import {
  Box,
  Center,
  Text,
  useColorModeValue,
  Image,
  Stack,
  Heading,
  Button,
} from "@chakra-ui/react";
import { signIn } from "next-auth/react";
import React from "react";

export const Welcome = () => {
  return (
    <Center py={16}>
      <Box
        maxW={"500px"}
        w={"full"}
        bg={useColorModeValue("white", "gray.800")}
        boxShadow={"2xl"}
        rounded={"md"}
        overflow={"hidden"}
      >
        <Image
          alt="Photo of a notebook with a pen on top of it."
          h="200px"
          w="full"
          src={
            "https://images.unsplash.com/photo-1517842645767-c639042777db?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"
          }
          objectFit="cover"
        />
        <Box p={6}>
          <Stack spacing="8" align="center" mb={5}>
            <Heading fontSize="3xl">Organize the small things</Heading>
            <Text px={4}>
              Lorem ipsum dolor, iusto sint culpa, assumenda laboriosam corporis
              quas atque doloremque sapiente porro nostrum voluptatibus
              molestiae accusamus quam! Voluptates quibusdam saepe beatae
              blanditiis itaque illo inventore corporis laborum eligendi vel.
              Minima repellat animi nisi. Aliquid, ex!
            </Text>
          </Stack>
          <Button
            w="full"
            py={7}
            mt={5}
            bg={useColorModeValue("black", "gray.900")}
            textColor="white"
            onClick={() => {
              let token = signIn();
              console.log(token)
            }}
          >
            Sign in
          </Button>
        </Box>
      </Box>
    </Center>
  );
};
