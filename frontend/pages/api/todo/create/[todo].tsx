import axios, { AxiosRequestConfig } from "axios";
import { NextApiRequest, NextApiResponse } from "next";
import { getSession } from "next-auth/react";

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
  const session = await getSession({ req });
  const { todo } = req.query;
  if (typeof todo == "string"){
    let taskName: string, tabName: string, date: string;
    [ tabName, taskName, date ] = todo.split("_");
    if (taskName === null || tabName === null || date === null ) {
      res.status(400).json({ messagee: "some of the input was null" });
    }
    if (session) {
      const config: AxiosRequestConfig = {
        method: "POST",
        headers: { Authorization: `Bearer ${session.user.backendToken}` },
      };

      return await axios
        .post(
          "http://0.0.0.0:8080/todo",
          {
            taskName: taskName,
            tabName: tabName,
            email: session.user.email,
            description: "todo",
            date: date,
            done: false
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
  res.status(400).json({message: "the input could not be parsed "});
};

export default handler;
