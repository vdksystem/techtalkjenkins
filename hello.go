package jenkins

import (
	"fmt"
	"net/http"
)

func hello(w http.ResponseWriter, r *http.Request) {
	fmt.Fprint(w, "Welcom to Lohika TechTalk!")
}
