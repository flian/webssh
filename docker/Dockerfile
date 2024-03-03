#FIXME just a place hoder,prepare for next release
FROM --platform=$TARGETPLATFORM alpine

LABEL maintainer="foylian <ljs2342003@gmail.com>"

ARG TARGETARCH

RUN apk add --no-cache tzdata libc6-compat gcompat

COPY result/webssh_linux_$TARGETARCH /webssh

ENTRYPOINT ["/webssh"]
