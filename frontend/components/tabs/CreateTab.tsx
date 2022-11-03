import { AddIcon } from "@chakra-ui/icons";
import {
  FormControl,
  FormLabel,
  FormErrorMessage,
  FormHelperText,
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

interface TabFormProps {
  pullTabsFromServer: () => any;
}

interface CreateTabProps {
  children: String;
  pullTabsFromServer: () => any;
}

export const CreateTab = ({ children, pullTabsFromServer }: CreateTabProps) => {
  return (
    <Popover>
      <PopoverTrigger>
        {/*
        <Button>{children}</Button>
        */}
        <IconButton
          aria-label="Create tab"
          colorScheme="none"
          color={'#38A169'}
          icon={<AddIcon />}
        />
      </PopoverTrigger>
      <PopoverContent>
        <PopoverHeader>Create Tab</PopoverHeader>
        <PopoverContent>
          <TabForm pullTabsFromServer={pullTabsFromServer} />
        </PopoverContent>
      </PopoverContent>
    </Popover>
  );
};

const TabForm = ({ pullTabsFromServer }: TabFormProps) => {
  const [input, setInput] = useState("");
  const inputChange = (e: {
    target: { value: React.SetStateAction<string> };
  }) => {
    setInput(e.target.value);
  };

  const inputError = input === "";

  const submitBtn = () => {
    if (inputError) return;
    fetch("/api/tabs/create/" + input).then(pullTabsFromServer());
  };

  return (
    //TODO submit on enter key
    <FormControl>
      <FormLabel>Tab Name</FormLabel>
      <Input type="tabName" value={input} onChange={inputChange} />
      <Button onClick={submitBtn}>Submit</Button>
    </FormControl>
  );
};
