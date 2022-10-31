import { Button, Center, Flex, List, ListItem, Spacer } from "@chakra-ui/react";
import { useSession } from "next-auth/react";
import Link from "next/link";
import { LoginBtn } from "./Login-btn";
import { Logo } from "./Logo";
import { NavBar } from "./NavBar";
import { NavLink } from "./NavLink";

const Header = () => {
  
  // TEST
  const { data: session } = useSession();
  const test = () => {
    console.log("test")
    console.log(session)
  }
  // TEST

  return (
    <Center backgroundColor="red" boxShadow="xl">
      <Flex direction="row" alignItems="center" px="23px" w="100vw" h="80px">
        <Logo />
        <Spacer />
        <Button onClick={test}>Test</Button>
        <NavBar/>
        <LoginBtn />
      </Flex>
    </Center>
  );
};

export default Header;
