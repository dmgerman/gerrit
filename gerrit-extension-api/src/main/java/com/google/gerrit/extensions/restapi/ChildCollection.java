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

begin_comment
comment|/**  * Nested collection of {@link RestResource}s below a parent.  *  * @param<P> type of the parent resource.  * @param<C> type of resource operated on by each view.  */
end_comment

begin_interface
DECL|interface|ChildCollection
specifier|public
interface|interface
name|ChildCollection
parameter_list|<
name|P
extends|extends
name|RestResource
parameter_list|,
name|C
extends|extends
name|RestResource
parameter_list|>
extends|extends
name|RestView
argument_list|<
name|P
argument_list|>
extends|,
name|RestCollection
argument_list|<
name|P
argument_list|,
name|C
argument_list|>
block|{ }
end_interface

end_unit

