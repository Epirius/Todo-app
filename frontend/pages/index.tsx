import {
  Box,
  Button,
  Center,
  Drawer,
  DrawerContent,
  DrawerHeader,
  DrawerOverlay,
  useDisclosure,
} from "@chakra-ui/react";
import type { NextPage } from "next";
import { useSession } from "next-auth/react";
import React from "react";
import Header from "../components/header/Header";
import { TabSelector } from "../components/tabs/TabSelector";
import { Welcome } from "../components/Welcome";

const Home: NextPage = () => {
  const { data: session } = useSession();
  const { isOpen, onOpen, onClose } = useDisclosure();


  if (!session) {
    return (
      <>
        <Header />
        <Welcome />
      </>
    );
  }
  
  return (
    <>
      <Header />
      <TabSelector/>

      <Box bgColor="purple">
        <Center height="80vhcd ">hello</Center>
      </Box>
    </>
  );
};

export default Home;