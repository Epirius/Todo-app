import React from "react";
import Image from "next/image";
import Link from "next/link";

export const Logo = () => {
  return (
    <Link href='/'>
      <Image src={require("../../assets/logo.svg").default} alt="Logo" />
    </Link>
  );
};
