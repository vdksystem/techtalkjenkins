package main

import (
	"fmt"
	"net/http"
)

func hello(w http.ResponseWriter, r *http.Request) {
	fmt.Fprint(w, "<h1>Welcom to Lohika TechTalk!</h1>")
}
