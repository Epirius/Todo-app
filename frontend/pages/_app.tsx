import type { AppProps } from "next/app";
import { ChakraProvider } from "@chakra-ui/react";
import Head from "next/head";
import Header from "../components/Header";
import {SessionProvider} from 'next-auth/react'
import { Session } from "next-auth";

function MyApp({ Component, pageProps }: AppProps<{session: Session;}>) {
  return (
    <>
      <SessionProvider session={pageProps.session}>
        <Head>
          <title>Todo app</title>
        </Head>
        <ChakraProvider>
          <Header />
          <Component {...pageProps} />
        </ChakraProvider>
      </SessionProvider>
    </>
  );
}

export default MyApp;
