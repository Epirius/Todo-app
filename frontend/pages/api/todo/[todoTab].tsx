import axios, { AxiosRequestConfig } from "axios";
import { NextApiRequest, NextApiResponse } from "next";
import { getSession } from "next-auth/react";
import { json } from "stream/consumers";


const handler = async (req: NextApiRequest, res: NextApiResponse) => {
    const session = await getSession({ req });
    const { todoTab } = req.query;
    if (session) {
      const config: AxiosRequestConfig = {
        method: "POST",
        headers: { Authorization: `Bearer ${session.user.backendToken}` },
      };
      return await axios
          .post(
            "http://0.0.0.0:8080/todo/get",
            {
                tabName: todoTab,
                email: session.user.email,
            },
            config
          )
          .then((response) => (res.status(response.status).json(response.data)))
          .catch((err) => {
            console.log(err);
            res.status(400).json(err);
          });
    }
}

export default handler;