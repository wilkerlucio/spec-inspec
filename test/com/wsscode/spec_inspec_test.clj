(ns com.wsscode.spec-inspec-test
  (:require [clojure.test :refer :all]
            [com.wsscode.spec-inspec :refer :all]
            [clojure.spec.alpha :as s]))

(s/def ::number int?)
(s/def ::number-strict (s/and ::number #(> % 10)))
(s/def ::derived ::number-strict)

(deftest test-safe-form
  (is (= (safe-form ::number) `int?))
  (is (nil? (safe-form ::invalid))))

(deftest test-form->spec
  (is (= (-> ::number safe-form form->spec) `int?))
  (is (= (-> ::number-strict safe-form form->spec) ::number)))

(deftest test-spec->root-sym
  (is (= (spec->root-sym ::number) `int?))
  (is (= (spec->root-sym ::number-strict) `int?))
  (is (= (spec->root-sym ::derived) `int?)))

(deftest test-parent-spec
  (is (nil? (parent-spec ::number)))
  (is (= (parent-spec ::number-strict) ::number))
  (is (= (parent-spec ::derived) ::number-strict)))

(deftest test-registry-lookup
  (is (= (registry-lookup {::number-strict :foo} ::number) nil))
  (is (= (registry-lookup {::number-strict :foo} ::number-strict) :foo))
  (is (= (registry-lookup {::number-strict :foo} ::derived) :foo)))


