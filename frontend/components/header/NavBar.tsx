import { Flex, List, ListItem } from "@chakra-ui/react";
import React from "react";
import { NavLink } from "./NavLink";

export const NavBar = () => {
  return (
    <nav>
      <List>
        <Flex direction="row" alignItems="center" px="23px" gap='1rem'>
          <ListItem>
            <NavLink link="/">App</NavLink>
          </ListItem>
          <ListItem>
            <NavLink link="about">About</NavLink>
          </ListItem>
        </Flex>
      </List>
    </nav>
  );
};
