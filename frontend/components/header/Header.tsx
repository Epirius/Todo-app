import { Button, Center, Flex, List, ListItem, Spacer } from "@chakra-ui/react";
import Link from "next/link";
import { LoginBtn } from "./Login-btn";
import { Logo } from "./Logo";
import { NavBar } from "./NavBar";
import { NavLink } from "./NavLink";

const Header = () => {
  return (
    <Center backgroundColor="red" boxShadow="xl">
      <Flex direction="row" alignItems="center" px="23px" w="100vw" h="80px">
        <Logo />
        <Spacer />
        <NavBar/>
        <LoginBtn />
      </Flex>
    </Center>
  );
};

export default Header;
