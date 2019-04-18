begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
import|;
end_import

begin_comment
comment|/** Populates initial NoteDb schema, {@code All-Projects} configuration, etc. */
end_comment

begin_interface
DECL|interface|SchemaCreator
specifier|public
interface|interface
name|SchemaCreator
block|{
comment|/**    * Create the schema, assuming it does not already exist.    *    *<p>Fails if the schema does exist.    *    * @throws IOException an error occurred.    * @throws ConfigInvalidException an error occurred.    */
DECL|method|create ()
name|void
name|create
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
function_decl|;
comment|/**    * Create the schema only if it does not already exist.    *    *<p>Succeeds if the schema does exist.    *    * @throws IOException an error occurred.    * @throws ConfigInvalidException an error occurred.    */
DECL|method|ensureCreated ()
name|void
name|ensureCreated
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
function_decl|;
block|}
end_interface

end_unit

