import axios, { AxiosRequestConfig } from "axios";
import { NextApiRequest, NextApiResponse } from "next";
import { getSession } from "next-auth/react";

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const session = await getSession({ req });
  if (session) {
    const config: AxiosRequestConfig = {
      method: "POST",
      headers: { Authorization: `Bearer ${session.user.backendToken}` },
    };

    return await axios
    .post( "http://0.0.0.0:8080/tab/getTabs", { email: session.user.email }, config)
    .then((response) => {
        res.status(200).json(response);
    })
    .catch((err) => {
        res.status(400).json({message: "hello", err});
    })
  }
};

export default handler;
