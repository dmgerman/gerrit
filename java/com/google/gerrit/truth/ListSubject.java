begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

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
name|Fact
operator|.
name|fact
import|;
end_import

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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Iterables
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
name|CustomSubjectBuilder
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
name|StandardSubjectBuilder
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BiFunction
import|;
end_import

begin_class
DECL|class|ListSubject
specifier|public
class|class
name|ListSubject
parameter_list|<
name|S
extends|extends
name|Subject
parameter_list|,
name|E
parameter_list|>
extends|extends
name|IterableSubject
block|{
DECL|field|list
specifier|private
specifier|final
name|List
argument_list|<
name|E
argument_list|>
name|list
decl_stmt|;
DECL|field|elementSubjectCreator
specifier|private
specifier|final
name|BiFunction
argument_list|<
name|StandardSubjectBuilder
argument_list|,
name|?
super|super
name|E
argument_list|,
name|?
extends|extends
name|S
argument_list|>
name|elementSubjectCreator
decl_stmt|;
DECL|method|assertThat ( List<E> list, Subject.Factory<? extends S, ? super E> subjectFactory)
specifier|public
specifier|static
parameter_list|<
name|S
extends|extends
name|Subject
parameter_list|,
name|E
parameter_list|>
name|ListSubject
argument_list|<
name|S
argument_list|,
name|E
argument_list|>
name|assertThat
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|list
parameter_list|,
name|Subject
operator|.
name|Factory
argument_list|<
name|?
extends|extends
name|S
argument_list|,
name|?
super|super
name|E
argument_list|>
name|subjectFactory
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|elements
argument_list|()
argument_list|)
operator|.
name|thatCustom
argument_list|(
name|list
argument_list|,
name|subjectFactory
argument_list|)
return|;
block|}
DECL|method|elements ()
specifier|public
specifier|static
name|CustomSubjectBuilder
operator|.
name|Factory
argument_list|<
name|ListSubjectBuilder
argument_list|>
name|elements
parameter_list|()
block|{
return|return
name|ListSubjectBuilder
operator|::
operator|new
return|;
block|}
DECL|method|ListSubject ( FailureMetadata failureMetadata, List<E> list, BiFunction<StandardSubjectBuilder, ? super E, ? extends S> elementSubjectCreator)
specifier|private
name|ListSubject
parameter_list|(
name|FailureMetadata
name|failureMetadata
parameter_list|,
name|List
argument_list|<
name|E
argument_list|>
name|list
parameter_list|,
name|BiFunction
argument_list|<
name|StandardSubjectBuilder
argument_list|,
name|?
super|super
name|E
argument_list|,
name|?
extends|extends
name|S
argument_list|>
name|elementSubjectCreator
parameter_list|)
block|{
name|super
argument_list|(
name|failureMetadata
argument_list|,
name|list
argument_list|)
expr_stmt|;
name|this
operator|.
name|list
operator|=
name|list
expr_stmt|;
name|this
operator|.
name|elementSubjectCreator
operator|=
name|elementSubjectCreator
expr_stmt|;
block|}
DECL|method|element (int index)
specifier|public
name|S
name|element
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|index
operator|>=
literal|0
argument_list|,
literal|"index(%s) must be>= 0"
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|isNotNull
argument_list|()
expr_stmt|;
if|if
condition|(
name|index
operator|>=
name|list
operator|.
name|size
argument_list|()
condition|)
block|{
name|failWithoutActual
argument_list|(
name|fact
argument_list|(
literal|"expected to have element at index"
argument_list|,
name|index
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|elementSubjectCreator
operator|.
name|apply
argument_list|(
name|check
argument_list|(
literal|"element(%s)"
argument_list|,
name|index
argument_list|)
argument_list|,
name|list
operator|.
name|get
argument_list|(
name|index
argument_list|)
argument_list|)
return|;
block|}
DECL|method|onlyElement ()
specifier|public
name|S
name|onlyElement
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
return|return
name|elementSubjectCreator
operator|.
name|apply
argument_list|(
name|check
argument_list|(
literal|"onlyElement()"
argument_list|)
argument_list|,
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|list
argument_list|)
argument_list|)
return|;
block|}
DECL|method|lastElement ()
specifier|public
name|S
name|lastElement
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|isNotEmpty
argument_list|()
expr_stmt|;
return|return
name|elementSubjectCreator
operator|.
name|apply
argument_list|(
name|check
argument_list|(
literal|"lastElement()"
argument_list|)
argument_list|,
name|Iterables
operator|.
name|getLast
argument_list|(
name|list
argument_list|)
argument_list|)
return|;
block|}
DECL|class|ListSubjectBuilder
specifier|public
specifier|static
class|class
name|ListSubjectBuilder
extends|extends
name|CustomSubjectBuilder
block|{
DECL|method|ListSubjectBuilder (FailureMetadata failureMetadata)
name|ListSubjectBuilder
parameter_list|(
name|FailureMetadata
name|failureMetadata
parameter_list|)
block|{
name|super
argument_list|(
name|failureMetadata
argument_list|)
expr_stmt|;
block|}
DECL|method|thatCustom ( List<E> list, Subject.Factory<? extends S, ? super E> subjectFactory)
specifier|public
parameter_list|<
name|S
extends|extends
name|Subject
parameter_list|,
name|E
parameter_list|>
name|ListSubject
argument_list|<
name|S
argument_list|,
name|E
argument_list|>
name|thatCustom
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|list
parameter_list|,
name|Subject
operator|.
name|Factory
argument_list|<
name|?
extends|extends
name|S
argument_list|,
name|?
super|super
name|E
argument_list|>
name|subjectFactory
parameter_list|)
block|{
return|return
name|that
argument_list|(
name|list
argument_list|,
parameter_list|(
name|builder
parameter_list|,
name|element
parameter_list|)
lambda|->
name|builder
operator|.
name|about
argument_list|(
name|subjectFactory
argument_list|)
operator|.
name|that
argument_list|(
name|element
argument_list|)
argument_list|)
return|;
block|}
DECL|method|that ( List<E> list, BiFunction<StandardSubjectBuilder, ? super E, ? extends S> elementSubjectCreator)
specifier|public
parameter_list|<
name|S
extends|extends
name|Subject
parameter_list|,
name|E
parameter_list|>
name|ListSubject
argument_list|<
name|S
argument_list|,
name|E
argument_list|>
name|that
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|list
parameter_list|,
name|BiFunction
argument_list|<
name|StandardSubjectBuilder
argument_list|,
name|?
super|super
name|E
argument_list|,
name|?
extends|extends
name|S
argument_list|>
name|elementSubjectCreator
parameter_list|)
block|{
return|return
operator|new
name|ListSubject
argument_list|<>
argument_list|(
name|metadata
argument_list|()
argument_list|,
name|list
argument_list|,
name|elementSubjectCreator
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

