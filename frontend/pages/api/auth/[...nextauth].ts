import NextAuth from "next-auth/next";
import GoogleProvider from "next-auth/providers/google";
import GitHubProvider from "next-auth/providers/github";
import { NextAuthOptions } from "next-auth";
import axios from "axios";

export const authOptions: NextAuthOptions = {
  session: {
    strategy: "jwt",
  },
  jwt: {
    maxAge: 60 * 60 * 24 * 30, // one month
  },
  providers: [
    GoogleProvider({
      clientId: process.env.GOOGLE_CLIENT_ID!,
      clientSecret: process.env.GOOGLE_CLIENT_SECRET!,
    }),
    GitHubProvider({
      clientId: process.env.GITHUB_ID!,
      clientSecret: process.env.GITHUB_SECRET!,
    }),
  ],
  secret: process.env.JWT_SECRET,

  callbacks: {
    async signIn({ user, account, profile, email, credentials }) {
      return axios
        .post("http://0.0.0.0:8080/login", {
          //process.env.BACKEND_URL! + "/login", {
          email: user.email,
          /*Headers: {
            Authorizeation: "Bearer " + account?.access_token
          }*/
        })
        .then((response) => {
          if (response.status == 200 && response.data.token !== null) {
            //TODO save token
            return true;
          }
          return false;
        })
        .catch((err) => {
          console.log("signin fetch failed: " + err);
          return false;
        });
    },
    async jwt({ token, user, account }) {
      if (user) {
        token.id = user.id;
        await axios
          .post("http://0.0.0.0:8080/login/token", {
            //process.env.BACKEND_URL! + "/login", {
            email: user.email,
          })
          .then((response) => {
            let backend_token: String = response.data.token;
            token.backend = backend_token;
          })
          .catch((err) => {
            console.log("[jwt] " + err);
            throw Error("Requesting the jwt token from the server failed");
          });
      }
      return token;
    },
    async session({ session, token }) {
      if (typeof token.backend === "string") {
        session.user.backendToken = token.backend;
      }
      return session;
    },
  },
};

export default NextAuth(authOptions);
