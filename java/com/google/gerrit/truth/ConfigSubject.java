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
DECL|package|com.google.gerrit.truth
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|truth
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertAbout
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableListMultimap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|BooleanSubject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|FailureMetadata
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|IntegerSubject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|IterableSubject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|LongSubject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|MultimapSubject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|StringSubject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Subject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|Nullable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|lib
operator|.
name|Config
import|;
end_import

begin_class
DECL|class|ConfigSubject
specifier|public
class|class
name|ConfigSubject
extends|extends
name|Subject
argument_list|<
name|ConfigSubject
argument_list|,
name|Config
argument_list|>
block|{
DECL|method|assertThat (Config config)
specifier|public
specifier|static
name|ConfigSubject
name|assertThat
parameter_list|(
name|Config
name|config
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|ConfigSubject
operator|::
operator|new
argument_list|)
operator|.
name|that
argument_list|(
name|config
argument_list|)
return|;
block|}
DECL|field|config
specifier|private
specifier|final
name|Config
name|config
decl_stmt|;
DECL|method|ConfigSubject (FailureMetadata metadata, Config actual)
specifier|private
name|ConfigSubject
parameter_list|(
name|FailureMetadata
name|metadata
parameter_list|,
name|Config
name|actual
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
argument_list|,
name|actual
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|actual
expr_stmt|;
block|}
DECL|method|sections ()
specifier|public
name|IterableSubject
name|sections
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"getSections()"
argument_list|)
operator|.
name|that
argument_list|(
name|config
operator|.
name|getSections
argument_list|()
argument_list|)
return|;
block|}
DECL|method|subsections (String section)
specifier|public
name|IterableSubject
name|subsections
parameter_list|(
name|String
name|section
parameter_list|)
block|{
name|requireNonNull
argument_list|(
name|section
argument_list|)
expr_stmt|;
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"getSubsections(%s)"
argument_list|,
name|section
argument_list|)
operator|.
name|that
argument_list|(
name|config
operator|.
name|getSubsections
argument_list|(
name|section
argument_list|)
argument_list|)
return|;
block|}
DECL|method|sectionValues (String section)
specifier|public
name|MultimapSubject
name|sectionValues
parameter_list|(
name|String
name|section
parameter_list|)
block|{
name|requireNonNull
argument_list|(
name|section
argument_list|)
expr_stmt|;
return|return
name|sectionValuesImpl
argument_list|(
name|section
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|subsectionValues (String section, String subsection)
specifier|public
name|MultimapSubject
name|subsectionValues
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|)
block|{
name|requireNonNull
argument_list|(
name|section
argument_list|)
expr_stmt|;
name|requireNonNull
argument_list|(
name|subsection
argument_list|)
expr_stmt|;
return|return
name|sectionValuesImpl
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|)
return|;
block|}
DECL|method|sectionValuesImpl (String section, @Nullable String subsection)
specifier|private
name|MultimapSubject
name|sectionValuesImpl
parameter_list|(
name|String
name|section
parameter_list|,
annotation|@
name|Nullable
name|String
name|subsection
parameter_list|)
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|ImmutableListMultimap
operator|.
name|Builder
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|b
init|=
name|ImmutableListMultimap
operator|.
name|builder
argument_list|()
decl_stmt|;
name|config
operator|.
name|getNames
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
literal|true
argument_list|)
operator|.
name|forEach
argument_list|(
name|n
lambda|->
name|Arrays
operator|.
name|stream
argument_list|(
name|config
operator|.
name|getStringList
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|n
argument_list|)
argument_list|)
operator|.
name|forEach
argument_list|(
name|v
lambda|->
name|b
operator|.
name|put
argument_list|(
name|n
argument_list|,
name|v
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|check
argument_list|(
literal|"getSection(%s, %s)"
argument_list|,
name|section
argument_list|,
name|subsection
argument_list|)
operator|.
name|that
argument_list|(
name|b
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
DECL|method|isEmpty ()
specifier|public
name|void
name|isEmpty
parameter_list|()
block|{
name|sections
argument_list|()
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|text ()
specifier|public
name|StringSubject
name|text
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"toText()"
argument_list|)
operator|.
name|that
argument_list|(
name|config
operator|.
name|toText
argument_list|()
argument_list|)
return|;
block|}
DECL|method|stringValues (String section, @Nullable String subsection, String name)
specifier|public
name|IterableSubject
name|stringValues
parameter_list|(
name|String
name|section
parameter_list|,
annotation|@
name|Nullable
name|String
name|subsection
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|requireNonNull
argument_list|(
name|section
argument_list|)
expr_stmt|;
name|requireNonNull
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"getStringList(%s, %s, %s)"
argument_list|,
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|)
operator|.
name|that
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|config
operator|.
name|getStringList
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|stringValue (String section, @Nullable String subsection, String name)
specifier|public
name|StringSubject
name|stringValue
parameter_list|(
name|String
name|section
parameter_list|,
annotation|@
name|Nullable
name|String
name|subsection
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|requireNonNull
argument_list|(
name|section
argument_list|)
expr_stmt|;
name|requireNonNull
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"getString(%s, %s, %s)"
argument_list|,
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|)
operator|.
name|that
argument_list|(
name|config
operator|.
name|getString
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|)
argument_list|)
return|;
block|}
DECL|method|intValue ( String section, @Nullable String subsection, String name, int defaultValue)
specifier|public
name|IntegerSubject
name|intValue
parameter_list|(
name|String
name|section
parameter_list|,
annotation|@
name|Nullable
name|String
name|subsection
parameter_list|,
name|String
name|name
parameter_list|,
name|int
name|defaultValue
parameter_list|)
block|{
name|requireNonNull
argument_list|(
name|section
argument_list|)
expr_stmt|;
name|requireNonNull
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"getInt(%s, %s, %s, %s)"
argument_list|,
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|,
name|defaultValue
argument_list|)
operator|.
name|that
argument_list|(
name|config
operator|.
name|getInt
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|,
name|defaultValue
argument_list|)
argument_list|)
return|;
block|}
DECL|method|longValue (String section, String subsection, String name, long defaultValue)
specifier|public
name|LongSubject
name|longValue
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|,
name|String
name|name
parameter_list|,
name|long
name|defaultValue
parameter_list|)
block|{
name|requireNonNull
argument_list|(
name|section
argument_list|)
expr_stmt|;
name|requireNonNull
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"getLong(%s, %s, %s, %s)"
argument_list|,
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|,
name|defaultValue
argument_list|)
operator|.
name|that
argument_list|(
name|config
operator|.
name|getLong
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|,
name|defaultValue
argument_list|)
argument_list|)
return|;
block|}
DECL|method|booleanValue ( String section, String subsection, String name, boolean defaultValue)
specifier|public
name|BooleanSubject
name|booleanValue
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|,
name|String
name|name
parameter_list|,
name|boolean
name|defaultValue
parameter_list|)
block|{
name|requireNonNull
argument_list|(
name|section
argument_list|)
expr_stmt|;
name|requireNonNull
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"getBoolean(%s, %s, %s, %s)"
argument_list|,
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|,
name|defaultValue
argument_list|)
operator|.
name|that
argument_list|(
name|config
operator|.
name|getBoolean
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|,
name|defaultValue
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit
