import { AxiosRequestConfig } from "axios";
import { NextApiRequest, NextApiResponse } from "next";
import { getSession } from "next-auth/react";


const handler = async (req: NextApiRequest, res: NextApiResponse) => {
    const session = await getSession({ req });
    if (session) {
      const config: AxiosRequestConfig = {
        method: "POST",
        headers: { Authorization: `Bearer ${session.user.backendToken}` },
      };
    }
}

export default handler;