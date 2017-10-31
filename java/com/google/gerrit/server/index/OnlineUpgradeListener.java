begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
package|;
end_package

begin_comment
comment|/** Listener for online schema upgrade events. */
end_comment

begin_interface
DECL|interface|OnlineUpgradeListener
specifier|public
interface|interface
name|OnlineUpgradeListener
block|{
comment|/**    * Called before starting upgrading a single index.    *    * @param name index definition name.    * @param oldVersion old schema version.    * @param newVersion new schema version.    */
DECL|method|onStart (String name, int oldVersion, int newVersion)
name|void
name|onStart
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|oldVersion
parameter_list|,
name|int
name|newVersion
parameter_list|)
function_decl|;
comment|/**    * Called after successfully upgrading a single index.    *    * @param name index definition name.    * @param oldVersion old schema version.    * @param newVersion new schema version.    */
DECL|method|onSuccess (String name, int oldVersion, int newVersion)
name|void
name|onSuccess
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|oldVersion
parameter_list|,
name|int
name|newVersion
parameter_list|)
function_decl|;
comment|/**    * Called after failing to upgrade a single index.    *    * @param name index definition name.    * @param oldVersion old schema version.    * @param newVersion new schema version.    */
DECL|method|onFailure (String name, int oldVersion, int newVersion)
name|void
name|onFailure
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|oldVersion
parameter_list|,
name|int
name|newVersion
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

