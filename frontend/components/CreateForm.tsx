import { AddIcon } from "@chakra-ui/icons";
import {
  FormControl,
  FormLabel,
  Button,
  Input,
  Popover,
  PopoverContent,
  PopoverHeader,
  PopoverTrigger,
  InputLeftElement,
  IconButton,
} from "@chakra-ui/react";
import React, { useState } from "react";
import { pathToFileURL } from "url";

interface FormButton {
  callbackFunc: (slug: String) => any;
  type: String;
}

interface FormProps {
  callbackFunc: (slug: String) => any;
  type: String;
}

export const CreateForm = ({ callbackFunc, type }: FormButton) => {
  let btn;
  if (type === "tab") {
    btn = (
      <IconButton
        aria-label="Create tab"
        colorScheme="none"
        color={"#38A169"}
        icon={<AddIcon />}
      />
    );
  } else if (type === "task") {
    btn = <Button aria-label="Create tab"> Create task </Button>;
  } else {
    btn = <Button>ERROR IMPLEMENT BUTTON TYPE</Button>;
  }

  return (
    <Popover>
      <PopoverTrigger>{btn}</PopoverTrigger>
      <PopoverContent>
        <PopoverHeader>Create {type}</PopoverHeader>
        <PopoverContent>
          <Form callbackFunc={callbackFunc} type={type} />
        </PopoverContent>
      </PopoverContent>
    </Popover>
  );
};

const Form = ({ callbackFunc, type }: FormProps) => {
  const [name, setName] = useState("");
  const [date, setDate] = useState("");

  const nameChange = (e: {
    target: { value: React.SetStateAction<string> };
  }) => {
    setName(e.target.value);
  };
  const dateChange = (e: {
    target: { value: React.SetStateAction<string> };
  }) => {
    setDate(e.target.value);
  };

  const inputError = name === "";

  const submitBtn = async () => {
    if (inputError || name === "") return;
    if (type === "task") callbackFunc(name + "_" + date);
    else if (type === "tab") callbackFunc(name);
    else throw Error("could not understand Type for submit btn");
  };

  let inpt = [];
  inpt.push(<FormLabel>Name</FormLabel>);
  inpt.push(<Input type="Name" value={name} onChange={nameChange} />);
  if (type === "task") {
    inpt.push(<FormLabel>Date</FormLabel>);
    inpt.push(<Input type="Date" value={date} onChange={dateChange} />);
  }

  return (
    <FormControl>
      {inpt}
      <Button onClick={submitBtn}>Submit</Button>
    </FormControl>
  );
};
