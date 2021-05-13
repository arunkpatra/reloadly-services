# Reloadly Helm Chart

## Notes

#### Install (Perform a Release)

``` 
$ helm install reloadly-dev ./reloadly --namespace reloadly --create-namespace --dry-run
# Or, run with overrides
$ helm install -f override.yaml reloadly-dev ./reloadly --namespace reloadly --create-namespace --dry-run
```

> The `-f override.yaml` is an override file which contain properties. These properties override the values in
`values.yaml`. Also, the `--create-namespace` works in HELM 3.2.0 and above. To .perform an actual release, remove the `--dry-run` parameter

#### Run Tests for a Release

``` 
$ helm test reloadly-dev --namespace reloadly --logs

NAME: reloadly-dev
LAST DEPLOYED: Tue May 11 13:46:57 2021
NAMESPACE: reloadly
STATUS: deployed
REVISION: 1
TEST SUITE:     reloadly-dev-admin-service-test
Last Started:   Tue May 11 13:54:35 2021
Last Completed: Tue May 11 13:54:45 2021
Phase:          Succeeded
TEST SUITE:     reloadly-dev-authentication-service-test
Last Started:   Tue May 11 13:54:45 2021
Last Completed: Tue May 11 13:54:52 2021
Phase:          Succeeded
NOTES:

```

#### Get Status of a Release

``` 
$ helm status reloadly-dev --namespace reloadly

NAME: reloadly-dev
LAST DEPLOYED: Tue May 11 13:46:57 2021
NAMESPACE: reloadly
STATUS: deployed
REVISION: 1
TEST SUITE:     reloadly-dev-admin-service-test
Last Started:   Tue May 11 13:54:35 2021
Last Completed: Tue May 11 13:54:45 2021
Phase:          Succeeded
TEST SUITE:     reloadly-dev-authentication-service-test
Last Started:   Tue May 11 13:54:45 2021
Last Completed: Tue May 11 13:54:52 2021
Phase:          Succeeded
NOTES:

```

#### List Resources created by a Release

``` 
$ helm get manifest reloadly-dev --namespace reloadly | kubectl get --namespace reloadly -f -

NAME                                  SECRETS   AGE
serviceaccount/reloadly-svc-account   1         10m

NAME                             TYPE       CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
service/admin-service            NodePort   10.103.136.28   <none>        9595:30095/TCP   10m
service/authentication-service   NodePort   10.98.139.169   <none>        9090:30090/TCP   10m

NAME                                     READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/admin-service            1/1     1            1           10m
deployment.apps/authentication-service   1/1     1            1           10m
```

#### Upgrade a Release

``` 
helm upgrade --install reloadly-dev ./reloadly --namespace reloadly --force
```

#### List all Releases

``` 
$ helm ls --namespace reloadly
NAME            NAMESPACE       REVISION        UPDATED                                 STATUS          CHART           APP VERSION
reloadly-dev    reloadly        1               2021-05-11 13:46:57.851863 +0530 IST    deployed        reloadly-1.0.0  1.1.0 
```

#### Show Release History

``` 
$ helm history reloadly-dev --namespace reloadly

REVISION        UPDATED                         STATUS          CHART           APP VERSION     DESCRIPTION     
1               Tue May 11 13:46:57 2021        deployed        reloadly-1.0.0  1.1.0           Install complete

```

#### Rollback a Release

``` 
$ helm rollback reloadly-dev 1 --namespace reloadly 

Rollback was a success! Happy Helming!

$ helm history reloadly-dev --namespace reloadly
```

#### Uninstall a Release

``` 
helm uninstall reloadly-dev --namespace reloadly
```

#### Storage

https://kubernetes.io/docs/tasks/configure-pod-container/configure-persistent-volume-storage/

List persistent volumes

``` 
$ kubectl get pv -n reloadly

NAME                                       CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS   CLAIM                          STORAGECLASS   REASON   AGE
pvc-0037a827-74ae-49aa-9a37-c76729096a61   8Gi        RWO            Delete           Bound    kafka/data-kafka-zookeeper-0   hostpath                3d14h
pvc-145db59b-4761-495b-8b27-68c00e8cd506   8Gi        RWO            Delete           Bound    kafka/data-kafka-0             hostpath                3d14h
pvc-eaa8ea32-e748-4758-94fa-61216b8999d9   8Gi        RWO            Delete           Bound    mysql/data-mysql-0             hostpath                3d16h

```

List persistent volume claims

#### Delete a Pod

``` 
$ kubectl delete pod <pod_name> --namespace reloadly
```