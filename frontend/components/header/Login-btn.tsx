import React from "react";
import { useSession, signIn, signOut } from "next-auth/react";
import { Button } from "@chakra-ui/react";
import { UnlockIcon } from "@chakra-ui/icons";

export function LoginBtn() {
  const { data: session } = useSession();
  console.log(session)
  if (session) {
    return (
      <Button colorScheme="cyan" size="sm" onClick={() => signOut()}>
        Sign out
      </Button>
    );
  }
  return (
    <Button colorScheme="cyan" size="sm" onClick={() => signIn()}>
      Sign in
    </Button>
  );
}
