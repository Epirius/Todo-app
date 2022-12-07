import axios, { AxiosRequestConfig } from "axios";
import { NextApiRequest, NextApiResponse } from "next";
import { getSession } from "next-auth/react";

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const session = await getSession({ req });
  const { tab, task, checkboxStatus } = req.query;
  if (tab === null || task === null || checkboxStatus === null) {
    res.status(400).json({ messagee: "some of the input was null" });
    return;
  }
  const url =
    "http://0.0.0.0:8080/todo/checkbox/" +
    tab +
    "/" +
    task +
    "/" +
    checkboxStatus;

  if (session) {
    const config: AxiosRequestConfig = {
      method: "POST",
      headers: { Authorization: `Bearer ${session.user.backendToken}` },
    };

    return await axios
      .post(url, {}, config)
      .then((response) => {
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
