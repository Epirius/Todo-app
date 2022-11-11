import { DeleteIcon } from "@chakra-ui/icons";
import { Button, Center, IconButton, Spacer, Text } from "@chakra-ui/react";
import React, { useCallback, useEffect, useState } from "react";
import { CreateForm } from "../CreateForm";
import { Todo } from "../todo/Todo";

interface tabPageProps {
  tabName: String;
  pullTabsFromServer: () => any;
}

interface todoObj{
  taskName: String;
  tabName: String;
  email: String;
  description: String;
  date: String;
  done: boolean;
}

export const TabPage = ({ tabName, pullTabsFromServer }: tabPageProps) => {
  const [tasks, setTasks] = useState(new Array<todoObj>());

  useEffect(() => {
    pullTasks();
  }, []);

  const pullTasks = async () => {
    await fetch("/api/todo/" + tabName)
      .then((res) => {
        if (res.status !== 200) throw Error();
        return res.json();
      })
      .then((res) => {
        setTasks(res);
      })
      .catch((err) => {
        console.log("could not fetch the tasks from backend. error: " + err);
      });
  };

  const deleteTab = async () => {
    fetch("/api/tabs/delete/" + tabName).then((res) => pullTabsFromServer());
  };
  const createTask = async (slug: String) => {
    fetch("/api/todo/create/" + tabName + "_" + slug).then((res) =>
      pullTasks()
    );
  };
  return (
    <>
      <Center padding="5px 20px">
        <Text as="u" fontSize="2.3rem">
          {tabName}:
        </Text>
        <Spacer />
        <Button onClick={pullTasks}>pull tasks</Button>
        <CreateForm callbackFunc={createTask} type="task" />
        <IconButton
          onClick={deleteTab}
          backgroundColor="red.400"
          marginLeft="1rem"
          icon={<DeleteIcon />}
          aria-label="Delete tab"
        />
      </Center>

      {tasks.map((task, index: number) => {
        return <Todo key={index} props={task} pullTask={pullTasks} />;
      })}
    </>
  );
};
