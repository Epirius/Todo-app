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
} from "@chakra-ui/react";
import React, { useState } from "react";

interface CreateTabProps {
  children: String;
}

export const CreateTab = ({ children }: CreateTabProps) => {
  return (
    <Popover>
      <PopoverTrigger>
        <Button>{children}</Button>
      </PopoverTrigger>
      <PopoverContent>
        <PopoverHeader>Create Tab</PopoverHeader>
        <PopoverContent>
          <TabForm />
        </PopoverContent>
      </PopoverContent>
    </Popover>
  );
};

const TabForm = () => {
  const [input, setInput] = useState("");
  const inputChange = (e: {
    target: { value: React.SetStateAction<string> };
  }) => {
    setInput(e.target.value);
  };

  const inputError = input === "";

  const submitBtn = () => {
    if (inputError) return;
    fetch("/api/tabs/create/" + input)
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
