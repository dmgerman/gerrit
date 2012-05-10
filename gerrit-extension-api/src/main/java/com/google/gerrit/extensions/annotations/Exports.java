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
DECL|package|com.google.gerrit.extensions.annotations
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|annotations
package|;
end_package

begin_comment
comment|/** Static constructors for {@link Export} annotations. */
end_comment

begin_class
DECL|class|Exports
specifier|public
specifier|final
class|class
name|Exports
block|{
comment|/** Create an annotation to export under a specific name. */
DECL|method|named (String name)
specifier|public
specifier|static
name|Export
name|named
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|ExportImpl
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|Exports ()
specifier|private
name|Exports
parameter_list|()
block|{   }
block|}
end_class

end_unit

