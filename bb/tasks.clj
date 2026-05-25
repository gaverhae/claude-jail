(ns tasks
  (:require [babashka.process :as p]
            [babashka.tasks :as tasks]))

(defn up
  "Start the claude-jail Lima VM."
  []
  (tasks/shell "limactl start --tty=false --name=claude-jail claude-jail.yaml")
  (-> (p/process ["cat" "./ssh/key"])
      (p/process ["limactl" "shell" "claude-jail" "bash" "-c"
                  "cat > ~/.ssh/id_ed25519 && chmod 600 ~/.ssh/id_ed25519"])))

(defn down
  "Destroy the claude-jail Lima VM."
  []
  (tasks/shell "limactl delete -f claude-jail"))

(defn ssh
  "Shell into the claude-jail Lima VM."
  []
  (tasks/shell "limactl shell claude-jail /home/linuxbrew/.linuxbrew/bin/zsh"))

(defn rotate
  "Generate a new SSH key. Will overwrite an existing one."
  []
  (tasks/shell "rm -r ssh")
  (tasks/shell "mkdir -p ssh")
  (tasks/shell "ssh-keygen -t ed25519 -C claude-code -f ssh/key -N ''")
  (tasks/shell "cat ssh/key.pub"))
