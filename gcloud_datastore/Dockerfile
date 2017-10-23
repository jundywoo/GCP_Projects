FROM google/cloud-sdk
MAINTAINER ivan.li

# Expose the default port
EXPOSE 8000

CMD ["gcloud","beta", "emulators", "datastore", "start","--project=pi-docker","--host-port", "gdatastore:8000","--consistency","1.0","--no-store-on-disk"]
