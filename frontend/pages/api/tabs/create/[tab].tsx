import axios, { AxiosRequestConfig } from "axios";
import type { NextApiRequest, NextApiResponse } from "next";
import { getSession } from "next-auth/react";

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const session = await getSession({ req });
  const { tab } = req.query;
  if (session) {
    const config: AxiosRequestConfig = {
      method: "POST",
      headers: { Authorization: `Bearer ${session.user.backendToken}` },
    };
    const json = JSON.stringify({
      tabName: tab,
      email: session.user.email,
    });

    return await axios
      .post(
        "http://0.0.0.0:8080/tab",
        {
          tabName: tab,
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
        res.status(400).json(err); //{ message: "server response error" });
      });
  }
};

export default handler;
