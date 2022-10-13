import { Center, Flex, Spacer } from "@chakra-ui/react";
import Link from "next/link";
import { LoginBtn } from "./login-btn";

const Header = () => {
  return (
    <Center backgroundColor='red'>
      <Flex direction='row' alignItems='center' px='23px'  w='100vw' h='80px'>
        <div>Todo app</div>
        <Spacer/>
        <nav>
          <ul>
            <li>
              <Link href="/">App</Link>
            </li>
            <li>
              <Link href="/about">About</Link>
            </li>
          </ul>
        </nav>
        <LoginBtn />
      </Flex>
    </Center>
  );
};

export default Header;
