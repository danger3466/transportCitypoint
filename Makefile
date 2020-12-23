docker:
	docker build -t pro.nvart/transport_citypoint .
	docker run -d --env-file docker.env -p 8080:8080 --rm pro.nvart/transport_citypoint
