import axios, { AxiosRequestConfig } from "axios";
import { NextApiRequest, NextApiResponse } from "next";
import { getSession } from "next-auth/react";

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const session = await getSession({ req });
  const { tab, task } = req.query;
  if (tab === null || task === null) {
    res.status(400).json({ messagee: "some of the input was null" });
    return;
  }
  const url = "http://0.0.0.0:8080/todo/checkbox/" + tab + "/" + task;

  if (session) {
    const config: AxiosRequestConfig = {
      method: "GET",
      headers: { Authorization: `Bearer ${session.user.backendToken}` },
    };

    return await axios
      .get(url, config)
      .then((response) => {
        res.status(response.status).json(response.data);
      })
      .catch((err) => {
        //console.log(err);
        console.log(err.status);
        res.status(400).json(err);
      });
  }
};

export default handler;
