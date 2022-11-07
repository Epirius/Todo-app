import { DeleteIcon } from "@chakra-ui/icons";
import { Button, Center, IconButton, Spacer, Text } from "@chakra-ui/react";
import React from "react";
import { CreateForm } from "../CreateForm";
import { Todo } from "../todo/Todo";

interface tabPageProps {
  tabName: String;
  pullTabsFromServer: () => any;
}

export const TabPage = ({ tabName, pullTabsFromServer }: tabPageProps) => {
  const deleteTab = async () => {
    fetch("/api/tabs/delete/" + tabName).then(res => pullTabsFromServer());
  };
  const createTask = async (slug: String) => {
    console.log("createTask click")
    fetch("/api/todo/create/" + tabName + "_" + slug)
    .then(pullTabsFromServer())
};
  return (
    <>
      <Center padding="5px 20px">
        <Text as="u" fontSize="2.3rem">
          {tabName}:
        </Text>
        <Spacer />
        <CreateForm callbackFunc={createTask} type='task'/>
        <IconButton
          onClick={deleteTab}
          backgroundColor="red.400"
          marginLeft="1rem"
          icon={<DeleteIcon />}
          aria-label="Delete tab"
        />
      </Center>

      <Todo />
    </>
  );
};
