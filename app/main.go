package app

import (
	"net/http"
)

func main() {
	http.HandleFunc("/", hello)

	http.ListenAndServe(":80", nil)
}
