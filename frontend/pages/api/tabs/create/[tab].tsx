import axios, { AxiosRequestConfig } from "axios";
import type { NextApiRequest, NextApiResponse } from "next";
import { getSession } from "next-auth/react";

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const session = await getSession({ req });
  const { tab } = req.query;
  const url = "http://0.0.0.0:8080/tab/" + tab;
  if (session) {
    console.log(session.user.backendToken)
    const config: AxiosRequestConfig = {
      method: "POST",
      headers: { Authorization: `Bearer ${session.user.backendToken}` },
    };

    return await axios
      .post(url, {},config)
      .then((response) => {
        console.log(response);
        res.status(response.status).json({ message: "server response ok" });
      })
      .catch((err) => {
        //console.log(err);
        console.log(err.status);
        res.status(400).json(err);
      });
  }
};

export default handler;
