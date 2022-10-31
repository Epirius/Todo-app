import axios from "axios";
import type { NextApiRequest, NextApiResponse } from "next";

const handler = async (req: NextApiRequest, res: NextApiResponse) => {
    return await axios
        .get("http://0.0.0.0:8080/status")
        .then(response => {
            res.status(response.status).json({message: "server response ok"});
        })
        .catch(err => {
            res.status(400).json({message: "an error occured"});
        })
}

export default handler;