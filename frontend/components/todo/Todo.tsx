import { DeleteIcon } from "@chakra-ui/icons";
import {
  Box,
  Text,
  Flex,
  Stack,
  HStack,
  Spacer,
  Checkbox,
  IconButton,
} from "@chakra-ui/react";
import React, { useState } from "react";

interface todoProps {
  props: {
    taskName: String;
    tabName: String;
    email: String;
    description: String;
    date: String;
    done: boolean;
  };
  pullTask: () => any;
}

export const Todo = ({ props, pullTask }: todoProps) => {
  let { taskName, tabName, email, description, date, done } = props;
  const [check, setCheck] = useState(done);

  const deleteTask = async () => {
    fetch("/api/todo/delete/" + tabName + "_" + taskName).then(
      pullTask()
    );
  };

  const checkBoxUpdate = () => {
    console.log("click")
    

    fetch("/api/todo/checkbox/" + tabName + "/" + taskName + "/" + !check)
    .then(res => {
      if (res.status === 200){
        fetch("/api/todo/checkbox/hello/test/getStatus")
        .then(res1 => res1.json())
        .then(res2 => setCheck(res2))
      }
    })
  }

  return (
    <Stack spacing="24px" padding="5px 20px">
      <Box
        backgroundColor="lightGray"
        borderRadius="5px"
        height="5rem"
        padding="1rem 2rem"
      >
        <HStack spacing="2rem">
          <h2>{taskName}:</h2>
          <Spacer />
          <Text>{date}</Text>
          <Checkbox isChecked={check} onChange={() => checkBoxUpdate()}>Done</Checkbox>
          <IconButton
            onClick={() => deleteTask()}
            backgroundColor="red.400"
            icon={<DeleteIcon />}
            aria-label="Delete tab"
          />
        </HStack>
      </Box>
    </Stack>
  );
};
