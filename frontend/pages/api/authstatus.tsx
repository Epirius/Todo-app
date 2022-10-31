import axios, { AxiosRequestConfig } from "axios";
import type { NextApiRequest, NextApiResponse } from "next";
import { Session } from "next-auth";
import { getSession } from "next-auth/react";

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const session = await getSession({ req });
  if (session) {
    const config: AxiosRequestConfig = { 
        method: 'GET',
        headers: { Authorization: `Bearer ${session.user.backendToken}` } };
    return await axios
      .get("http://0.0.0.0:8080/authstatus", config)
      .then((response) => {
        res.status(response.status).json({ message: "server response ok" });
      })
      .catch((err) => {
        console.log(err);
        res.status(400).json({ message: "an error occured" });
      });
  } else {
    res
      .status(400)
      .json({ message: "could not get the session token from nextjs." });
  }
};

export default handler;
