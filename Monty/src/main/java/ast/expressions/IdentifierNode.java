/*
Copyright 2018-2019 Szymon Perlicki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package ast.expressions;

public final class IdentifierNode {
    boolean isFunctionCall() {
        return isFunctionCall;
    }

    private void setFunctionCall(boolean functionCall) {
        isFunctionCall = functionCall;
    }

    private boolean isFunctionCall;
    private String name;

    public IdentifierNode(String name, boolean isFunctionCall) {
        setName(name);
        setFunctionCall(isFunctionCall);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}