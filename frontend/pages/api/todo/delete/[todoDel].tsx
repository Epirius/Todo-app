import axios, { AxiosRequestConfig } from "axios";
import { NextApiRequest, NextApiResponse } from "next";
import { getSession } from "next-auth/react";


const handler = async (req: NextApiRequest, res: NextApiResponse) => {
    const session = await getSession({ req });
    const { todoDel } = req.query;
    let taskName: string, tabName: string;
    if (typeof todoDel == "string") {
      [tabName, taskName] = todoDel.split("_");
      if (taskName === null || tabName === null) {
        res.status(400).json({ messagee: "some of the input was null" });
        return;
      }
    } else {
      res.status(400).json({message: "the input could not be parsed "});
      return;
    }

    if (session) {
        const config: AxiosRequestConfig = {
          method: "DELETE",
          headers: { Authorization: `Bearer ${session.user.backendToken}` },
        };
  
        return await axios
          .delete(
            "http://0.0.0.0:8080/todo/" + tabName + "/" + taskName,
            config
          )
          .then((response) => {
            console.log(response);
            res.status(response.status).json({ message: "server response ok" });
          })
          .catch((err) => {
            console.log(err);
            res.status(400).json(err);
          });
    }
}

export default handler;