import type { AppProps } from "next/app";
import { Box, ChakraProvider, Flex, Spacer } from "@chakra-ui/react";
import Head from "next/head";
import { SessionProvider } from "next-auth/react";
import { Session } from "next-auth";
import Header from "../components/header/Header";
import { Footer } from "../components/Footer";

function MyApp({ Component, pageProps }: AppProps<{ session: Session }>) {
  return (
    <>
      <SessionProvider session={pageProps.session}>
        <Head>
          <title>Todo app</title>
        </Head>
        <ChakraProvider>
          <Box height="9vh">
            <Header />
          </Box>
          <Box height="88vh">
            <Component {...pageProps} />
          </Box>
          <Box height='3vh'>

            <Footer />
          </Box>
        </ChakraProvider>
      </SessionProvider>
    </>
  );
}

export default MyApp;
