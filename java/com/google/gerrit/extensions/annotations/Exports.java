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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoAnnotation
import|;
end_import

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
annotation|@
name|AutoAnnotation
DECL|method|named (String value)
specifier|public
specifier|static
name|Export
name|named
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
operator|new
name|AutoAnnotation_Exports_named
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/** Create an annotation to export based on a cannonical class name. */
DECL|method|named (Class<?> clazz)
specifier|public
specifier|static
name|Export
name|named
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
name|named
argument_list|(
name|clazz
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
return|;
block|}
DECL|method|Exports ()
specifier|private
name|Exports
parameter_list|()
block|{}
block|}
end_class

end_unit

