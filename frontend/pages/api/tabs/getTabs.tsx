import axios, { AxiosRequestConfig } from "axios";
import { NextApiRequest, NextApiResponse } from "next";
import { getSession } from "next-auth/react";

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const session = await getSession({ req });
  const url = "http://0.0.0.0:8080/tab";
  if (session) {
    const config: AxiosRequestConfig = {
      method: "GET",
      headers: { Authorization: `Bearer ${session.user.backendToken}` },
    };

    return await axios
      .get( url, config )
      .then((response) => {
        res.status(response.status).json(response.data)
      })
      .catch((err) => {
        res.status(400).json(err);
      });
  }
};

export default handler;
