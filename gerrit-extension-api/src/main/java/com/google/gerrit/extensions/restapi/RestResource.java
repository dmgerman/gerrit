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
DECL|package|com.google.gerrit.extensions.restapi
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_comment
comment|/**  * Generic resource handle defining arguments to views.  *<p>  * Resource handle returned by {@link RestCollection} and passed to a  * {@link RestView} such as {@link RestReadView} or {@link RestModifyView}.  */
end_comment

begin_interface
DECL|interface|RestResource
specifier|public
interface|interface
name|RestResource
block|{
comment|/** A resource with a last modification date. */
DECL|interface|HasLastModified
specifier|public
interface|interface
name|HasLastModified
block|{
DECL|method|getLastModified ()
specifier|public
name|Timestamp
name|getLastModified
parameter_list|()
function_decl|;
block|}
block|}
end_interface

end_unit

