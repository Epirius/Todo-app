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
      }
    } else {
      res.status(400).json({message: "the input could not be parsed "});
      return;
    }

    if (session) {
        const config: AxiosRequestConfig = {
          method: "POST",
          headers: { Authorization: `Bearer ${session.user.backendToken}` },
        };
  
        return await axios
          .post(
            "http://0.0.0.0:8080/todo/delete",
            {
                tabName: tabName,
                taskName: taskName,
                email: session.user.email,
            },
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