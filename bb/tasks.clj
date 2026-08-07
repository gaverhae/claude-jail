(ns tasks
  (:require [clojure.string :as str]
            [babashka.process :as p]
            [babashka.tasks :as tasks]))

(def host-loopback-ip "127.0.0.2")

(defn- require-host-loopback []
  (let [lo0 (:out @(p/process ["/sbin/ifconfig" "lo0"] {:out :string}))]
    (when-not (str/includes? lo0 (str "inet " host-loopback-ip " "))
      (binding [*out* *err*]
        (println host-loopback-ip "is not configured on the host loopback interface.")
        (println "Run this command, then retry `bb up`:")
        (println)
        (println "  sudo /sbin/ifconfig lo0 alias" host-loopback-ip "up"))
      (System/exit 1))))

(defn up
  "Start the claude-jail Lima VM."
  []
  (require-host-loopback)
  (tasks/shell "limactl start --tty=false --name=claude-jail claude-jail.yaml")
  (-> (p/process ["cat" "./ssh/key"])
      (p/process ["limactl" "shell" "claude-jail" "bash" "-c"
                  "cat > ~/.ssh/id_ed25519 && chmod 600 ~/.ssh/id_ed25519"])
      deref))

(defn down
  "Destroy the claude-jail Lima VM."
  []
  (tasks/shell "limactl delete -f claude-jail"))

(defn ssh
  "Shell into the claude-jail Lima VM."
  []
  (tasks/shell "limactl shell claude-jail su -l gary"))

(defn rotate
  "Generate a new SSH key. Will overwrite an existing one."
  []
  (tasks/shell "rm -r ssh")
  (tasks/shell "mkdir -p ssh")
  (tasks/shell "ssh-keygen -t ed25519 -C claude-code -f ssh/key -N ''")
  (tasks/shell "cat ssh/key.pub"))
