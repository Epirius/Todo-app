import { Box, Button, Flex, Stack, HStack, Spacer } from "@chakra-ui/react";
import React from "react";

interface todoProps {
  props: {
    taskName: String;
    tabName: String;
    email: String;
    description: String;
    date: String;
    done: Boolean;
  };
}

export const Todo = ({ props }: todoProps) => {
  let { taskName, tabName, email, description, date, done } = props;
  return (
    <Stack spacing="24px" padding="5px 20px">
      <Box
        backgroundColor="lightGray"
        borderRadius="5px"
        height="5rem"
        padding="1rem 2rem"
      >
        <HStack>
          <h2>{taskName}:</h2>
          <Spacer />
          <Button>Done</Button>
          <Button>Delete</Button>
        </HStack>
      </Box>
    </Stack>
  );
};
