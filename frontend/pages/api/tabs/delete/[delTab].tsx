import axios, { AxiosRequestConfig } from "axios";
import { NextApiRequest, NextApiResponse } from "next";
import { getSession } from "next-auth/react";


const handler = async (req: NextApiRequest, res: NextApiResponse) => {
    const session = await getSession({ req });
    const { delTab } = req.query;

    if (session) {
        const config: AxiosRequestConfig = {
          method: "DELETE",
          headers: { Authorization: `Bearer ${session.user.backendToken}` },
        };
  
        return await axios
          .delete(
            "http://0.0.0.0:8080/tab/" + delTab,
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