begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.systemstatus
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|systemstatus
package|;
end_package

begin_comment
comment|/** Exports current server information to an extension. */
end_comment

begin_interface
DECL|interface|ServerInformation
specifier|public
interface|interface
name|ServerInformation
block|{
comment|/** Current state of the server. */
DECL|enum|State
specifier|public
enum|enum
name|State
block|{
comment|/**      * The server is starting up, and network connections are not yet being      * accepted. Plugins or extensions starting during this time are starting      * for the first time in this process.      */
DECL|enumConstant|STARTUP
name|STARTUP
block|,
comment|/**      * The server is running and handling requests. Plugins starting during this      * state may be reloading, or being installed into a running system.      */
DECL|enumConstant|RUNNING
name|RUNNING
block|,
comment|/**      * The server is attempting a graceful halt of operations and will exit (or      * be killed by the operating system) soon.      */
DECL|enumConstant|SHUTDOWN
name|SHUTDOWN
block|;   }
DECL|method|getState ()
name|State
name|getState
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

