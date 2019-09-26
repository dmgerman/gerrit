begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.mail.send
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
operator|.
name|send
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|annotations
operator|.
name|ExtensionPoint
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Extension point to provide soy templates that should be registered so that they can be used for  * sending emails from a plugin.  */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|MailSoyTemplateProvider
specifier|public
interface|interface
name|MailSoyTemplateProvider
block|{
comment|/**    * Return the name of the resource path that contains the soy template files that are returned by    * {@link #getFileNames()}.    *    * @return resource path of the templates    */
DECL|method|getPath ()
name|String
name|getPath
parameter_list|()
function_decl|;
comment|/**    * Return the names of the soy template files.    *    *<p>These files are expected to exist in the resource path that is returned by {@link    * #getPath()}.    *    * @return names of the template files, including the {@code .soy} file extension    */
DECL|method|getFileNames ()
name|Set
argument_list|<
name|String
argument_list|>
name|getFileNames
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

