import { Box, Button, Flex, Stack, HStack, Spacer } from "@chakra-ui/react";
import React from "react";

export const Todo = () => {
  return (
    <Stack spacing="24px" padding="5px 20px">
      <Box
        backgroundColor="lightGray"
        borderRadius="5px"
        height="5rem"
        padding="1rem 2rem"
      >
        <HStack>
          <h2>Lorem ipsum dolor sit amet Qui, voluptate!</h2>
          <Spacer/>
          <Button>Done</Button>
          <Button>Delete</Button>
        </HStack>
      </Box>
      <Box backgroundColor="lightGray">get stuff</Box>
      <Box backgroundColor="lightGray">homework</Box>
    </Stack>
  );
};
