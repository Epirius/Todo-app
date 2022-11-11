import { DeleteIcon } from "@chakra-ui/icons";
import { Button, Center, IconButton, Spacer, Text } from "@chakra-ui/react";
import React, { useCallback, useEffect, useState } from "react";
import { CreateForm } from "../CreateForm";
import { Todo } from "../todo/Todo";

interface tabPageProps {
  tabName: String;
  pullTabsFromServer: () => any;
}

export const TabPage = ({ tabName, pullTabsFromServer }: tabPageProps) => {
  const [tasks, setTasks] = useState([
    {
      taskName: "NO TASK FOUND",
      tabName: tabName,
      email: "NO EMAIL FOUND",
      description: "NO DESCRIPTION",
      date: "1970-01-01",
      done: false,
    },
  ]);

  useEffect(() => {
    pullTasks();
  }, []);


  const pullTasks = async () => {
    fetch("/api/todo/" + tabName)
      .then((res) => res.json())
      .then((res) => {
        //console.log(res.status)
        if (res.status === 200){ // todo fix bug that returns status 400
          setTasks(res);
        }
      })
      .catch((err) => {
        console.log("could not fetch the tasks from backend. error: " + err);
        setTasks([
          {
            taskName: "NO TASK FOUND",
            tabName: tabName,
            email: "NO EMAIL FOUND",
            description: "NO DESCRIPTION",
            date: "1970-01-01",
            done: false,
          },
        ]);
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
