import NextAuth from "next-auth/next";
import GoogleProvider from "next-auth/providers/google";
import GitHubProvider from "next-auth/providers/github";
import { NextAuthOptions } from "next-auth";
import { signIn } from "next-auth/react";

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
    async signIn({ user, account }) {
      return fetch("http://0.0.0.0:8080/login", {
        //process.env.BACKEND_URL! + "/login", {
        method: "POST",
        headers: { "Content-Type": "application/json;charset=UTF-8" },
        body: JSON.stringify({
          email: user.email,
        }),
      }).then(
        () => true,
        (err) => {
          console.log("signin fetch failed: " + err);
          return false;
        }
      );
    },
  },
};

export default NextAuth(authOptions);
