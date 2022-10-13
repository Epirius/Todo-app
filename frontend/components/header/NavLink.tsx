import { Button } from "@chakra-ui/react";
import Link from "next/link";
import React from "react";

interface NavProps {
  link: string;
  children: string;
}

export const NavLink = ({ link, children }: NavProps) => {
  return (
    <Link href={link}>
      <Button colorScheme="black" variant="link" size='lg'>
        {children}
      </Button>
    </Link>
  );
};
