import { Button, Center, Flex, List, ListItem, Spacer } from "@chakra-ui/react";
import { useSession } from "next-auth/react";
import Link from "next/link";
import { LoginBtn } from "./Login-btn";
import { Logo } from "./Logo";
import { NavBar } from "./NavBar";
import { NavLink } from "./NavLink";
import { TestButton } from "./TestButton";

const Header = () => {
  
  // TEST
  const { data: session } = useSession();
  // TEST

  return (
    <Center backgroundColor="red" boxShadow="xl">
      <Flex direction="row" alignItems="center" px="23px" w="100vw" h="80px">
        <Logo />
        <Spacer />
        <TestButton />
        <NavBar/>
        <LoginBtn />
      </Flex>
    </Center>
  );
};

export default Header;
